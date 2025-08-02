package com.expensetracker.serviceimpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.ChangePasswordDTO;
import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.Role;
import com.expensetracker.entity.Tenant;
import com.expensetracker.entity.User;
import com.expensetracker.enums.Roles;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.UserSortField;
import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.jwtsecurity.UserDetailsImpl;
import com.expensetracker.mappers.UserMapper;
import com.expensetracker.repository.RoleRepository;
import com.expensetracker.repository.TenantRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.UserService;
import com.expensetracker.util.CommonConstants;
import com.expensetracker.util.QueryConstants;
import com.expensetracker.util.Utils;


@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private TenantRepository tenantRepository;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public UserServiceImpl(UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,
			TenantRepository tenantRepository,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.tenantRepository = tenantRepository;
		this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new CustomGraphQLException(400, "User " + userDTO.getEmail() + " Already Exists");
		}
		
	    // Role check: only SUPER_ADMIN can create SUPER_ADMIN or ADMIN
	    if (isCreatingAdminOrSuperAdmin(userDTO)) {
	        User currentUser = Utils.getCurrentUser();
	        if (!Utils.isSuperAdmin(currentUser.getRoles())) {
	            throw new CustomGraphQLException(403, "Only SUPER_ADMIN can create another SUPER_ADMIN or ADMIN");
	        }
	    }
	    
		User user = UserMapper.toUserEntity.apply(userDTO);
		
		//Check If TenantId is 0 set Global Tenant to the user
	    if(userDTO.getTenantId() == 0) {
	    	tenantRepository.findByName(CommonConstants.GLOBAL_TENANT)
	    		.ifPresent(tenant -> user.setTenant(tenant));
	    }
		  
		//If ADMIN is trying to add then set his tenantid
		User currentUser = Utils.getCurrentUser();
		if(Utils.isAdmin(currentUser.getRoles())) {
			tenantRepository.findById(currentUser.getTenant().getId())
					.ifPresentOrElse(tenant -> user.setTenant(tenant), () -> {
						throw new CustomGraphQLException(400, "Tenant not found for the current user");
					});
		}
		user.setRoles(saveUserRoles.apply(user.getRoles()));
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		User savedUser = userRepository.save(user);
		return UserMapper.toUserDTO.apply(savedUser);
	}
	
	private boolean isCreatingAdminOrSuperAdmin(UserDTO userDTO) {
	    return userDTO.getRoles() != null && userDTO.getRoles().stream()
	        .anyMatch(role -> role.getRoleName() == Roles.SUPER_ADMIN || role.getRoleName() == Roles.ADMIN);
	}





	UnaryOperator<Set<Role>> saveUserRoles = rolesFromUI -> {
		logger.debug("Roles from UI: {}", rolesFromUI);
		Set<Role> roles = new HashSet<>();

		for (Role role : rolesFromUI) {
			if (role.getRoleName() == null) {
				// Skip roles with null role names to avoid any issues
				continue;
			}

			// Try to find the role by name
			Optional<Role> savedRole = roleRepository.findByRoleName(role.getRoleName());

			if (savedRole.isPresent()) {
				// If the role already exists in the database, add it to the set
				roles.add(savedRole.get());
			} else {
				// If the role does not exist, save it to the database and add it to the set
				Role newRole = roleRepository.save(role);
				roles.add(newRole);
			}
		}
		logger.debug("Final roles set: {}", roles);
		return roles;
	};

	@Override
	public UserDTO updateUser(UserDTO userDTO) {
		User user = userRepository.findById(userDTO.getId())
				.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
		Optional.ofNullable(userDTO.getFirstName()).ifPresent(user::setFirstName);
		Optional.ofNullable(userDTO.getLastName()).ifPresent(user::setLastName);
		Optional.ofNullable(userDTO.getGendar()).ifPresent(user::setGendar);
		Optional.ofNullable(userDTO.getEmail()).ifPresent(user::setEmail);
		Optional.ofNullable(userDTO.getPhoneNumber()).ifPresent(user::setPhoneNumber);
		if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
		    if (userRepository.existsByEmail(userDTO.getEmail())) {
		        throw new CustomGraphQLException(400, "Email already in use");
		    }
		    user.setEmail(userDTO.getEmail());
		}
		//Only SUPER_ADMIN can update roles
		if (isCreatingAdminOrSuperAdmin(userDTO)) {
			User currentUser = Utils.getCurrentUser();
			if (!Utils.isSuperAdmin(currentUser.getRoles())) {
				throw new CustomGraphQLException(403, "Only SUPER_ADMIN can update roles");
			}
		}
		Tenant tenant = tenantRepository.findById(userDTO.getTenantId())
				.orElseThrow(() -> new CustomGraphQLException(400, "Tenant with ID " + userDTO.getTenantId()+ " does not exist"));
		user.setTenant(tenant);
		user.setRoles(saveUserRoles.apply(userDTO.getRoles()));
		try {
			 userRepository.save(user);
		} catch (Exception e) {
			logger.error("Error While Updating User :: {}", e.getMessage());
			e.printStackTrace();
		}
		return UserMapper.toUserDTO.apply(user);
	}

	@Override
	public String deleteUser(Long userId) {
		if (userId == null) {
			throw new CustomGraphQLException(400, "User ID cannot be null");
		}
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails.getUser().getId().equals(userId)) {
			throw new CustomGraphQLException(403, "You cannot delete your own account");
		}
		// If the user is trying to delete another user's account, we need to check their ROLE
		if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
			// If the user is an ADMIN, they can only delete users from their own tenant
			Long tenantId = userDetails.getUser().getTenant().getId();
			if (tenantId == null || tenantId == 0) {
				throw new CustomGraphQLException(403, "Admins can only delete users from their own tenant");
			}
			User user = userRepository.findByIdAndTenantId(userId, tenantId)
					.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
			userRepository.delete(user);
			return "User deleted successfully";
		}
        return CommonConstants.USER_NOT_FOUND;
	}

	@Override
	public UserDTO getUser(Long userId) {
		if (userId == null) {
			throw new CustomGraphQLException(400, "User ID cannot be null");
		}
		User currentUser = Utils.getCurrentUser();

		if (Utils.isSuperAdmin(currentUser.getRoles())) {
			return userRepository.findById(userId)
								.map(UserMapper.toUserDTO)
								.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
		} else if (Utils.isAdmin(currentUser.getRoles())) {
			Long tenantId = currentUser.getTenant().getId();
			if (tenantId == null || tenantId == 0) {
				throw new CustomGraphQLException(403, "Admins can only access users from their own tenant");
			}
			User user = userRepository.findByIdAndTenantId(userId, tenantId)
					.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
			return UserMapper.toUserDTO.apply(user);
		} else if (!currentUser.getId().equals(userId)) {
			throw new CustomGraphQLException(403, "You do not have permission to access this user's data");
		}
		User user = userRepository.findById(userId)
									.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
		return UserMapper.toUserDTO.apply(user);
	}

	@Override
	public PaginatedResponse<List<UserDTO>> getAllUsers(int pageSize, int pageNumber, UserSortField sortBy, SortDirection direction) {
		Sort sort = Sort.by(direction == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy.name());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		//Here i want to implement logic like if the user is SUPER_ADMIN then he can see all users And ADMIN Can see only users of his tenant
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Long tenantId = userDetails.getUser().getTenant().getId();
		Page<User> page=null;
		if (Utils.isSuperAdmin(userDetails.getUser().getRoles())) {
			// If tenantId is null or 0, it means the user is a SUPER_ADMIN
			page = userRepository.findAll(pageable);
		} else if(Utils.isAdmin(userDetails.getUser().getRoles())) {
			// If tenantId is not null or 0, it means the user is an ADMIN
			page = userRepository.findByTenantId(tenantId, pageable);
		} else {
			// If the user is neither SUPER_ADMIN nor ADMIN, throw an exception
			throw new CustomGraphQLException(403, "You do not have permission to view users");
		}

		List<User> users = page.getContent().isEmpty()?List.of():page.getContent();
		long totalCount = page.getTotalElements();
		List<UserDTO> list = users.stream().map(UserMapper.toUserDTO::apply).toList();
		return new PaginatedResponse<>(list, totalCount);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public String updatePassword(ChangePasswordDTO changePasswordDTO) {
		final String FIND_BY_USER_ID = " SELECT u.id, u.email, u.password, u.first_name, u.last_name, u.phone_number FROM users as u WHERE u.id =:id ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", changePasswordDTO.id());
		UserDTO user=null;
		try {
			user = namedParameterJdbcTemplate.queryForObject(FIND_BY_USER_ID, params, (rs, rowNum) -> {
				UserDTO u = new UserDTO();
				u.setId(rs.getLong("id"));
				u.setFirstName(rs.getString("first_name"));
				u.setLastName(rs.getString("last_name"));
				//u.setPhoneNumber(rs.getString("phone_number"));
				u.setPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				return u;
			});
		} catch (DataAccessException e) {
			logger.error("Getting Exception While Getting user :: {}", e.getMessage());
		}
		if (user == null) {
			throw new CustomGraphQLException(400, "User Dosn't Exists");
		}
		
		// USER Can change his own password and SUPER_ADMIN can change any user's password
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// Check if the old password matches
		if (!Utils.isSuperAdmin(userDetails.getUser().getRoles()) && !passwordEncoder.matches(changePasswordDTO.oldPassword(), user.getPassword())) {
			throw new CustomGraphQLException(400, "Old Password is Incorrect");
		}


		if (!userDetails.getUser().getId().equals(user.getId())
				&& !Utils.isSuperAdmin(userDetails.getUser().getRoles())) {
			throw new CustomGraphQLException(403, "You do not have permission to change this user's password");
		}
		MapSqlParameterSource updatePasswordMap = new MapSqlParameterSource();
		String updatedPassword = passwordEncoder.encode(changePasswordDTO.password());
		updatePasswordMap.addValue("password", updatedPassword);
		updatePasswordMap.addValue("id", user.getId());

		int updateRowCount = namedParameterJdbcTemplate.update(QueryConstants.UPDATE_PASSWORD, updatePasswordMap);
		if(updateRowCount==1)
			return "Password Changed Successfully";
		else
			return "Password not updated";
	}

	@Override
	public User findByEmailWithRoles(String email) {
		return userRepository.findByEmailWithRoles(email).orElseThrow(()->new CustomGraphQLException(400, " User not found with mail : "+email));
	}

}
