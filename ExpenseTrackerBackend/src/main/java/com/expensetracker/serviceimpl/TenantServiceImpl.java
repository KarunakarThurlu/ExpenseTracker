package com.expensetracker.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.TenantDTO;
import com.expensetracker.entity.Tenant;
import com.expensetracker.entity.User;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.TenantSortField;
import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.mappers.TenantMapper;
import com.expensetracker.repository.TenantRepository;
import com.expensetracker.service.TenantService;
import com.expensetracker.util.Utils;

@Service
public class TenantServiceImpl implements TenantService{
	
	private final TenantRepository tenantRepository;
	
	public TenantServiceImpl(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}

	@Override
	public TenantDTO saveTenant(TenantDTO tenantDTO) {
		//SuperAdmin can create a tenant without an ID, so we ensure the ID is null
		User currentUser = Utils.getCurrentUser();
		if (currentUser == null || !Utils.isSuperAdmin(currentUser.getRoles())) {
			throw new CustomGraphQLException(403, "Only SuperAdmin can create a tenant");
		}
		tenantDTO.setId(null); // Ensure ID is null for new tenant
		Tenant apply = TenantMapper.toEntity.apply(tenantDTO);
		Tenant savedTenant = tenantRepository.save(apply);
		return TenantMapper.toDTO.apply(savedTenant);
	}

	@Override
	public TenantDTO fetchTenant(Long id) {
		if (id == null) {
			throw new CustomGraphQLException(400, "Tenant ID is required");
		}
		//ADMIN can fetch only their own tenant, while SuperAdmin can fetch any tenant
		User currentUser = Utils.getCurrentUser();
		if (currentUser == null) {
			throw new CustomGraphQLException(403, "Unauthorized user");
		}
		if (!Utils.isSuperAdmin(currentUser.getRoles()) && !currentUser.getTenant().getId().equals(id)) {
			throw new CustomGraphQLException(403, "You do not have permission to access this tenant");
		}
		// Fetch tenant by ID
		Tenant tenant = tenantRepository.findById(id)
			.orElseThrow(() -> new CustomGraphQLException(400, "Tenant with ID " + id + " does not exist"));
		return TenantMapper.toDTO.apply(tenant);
	}

	@Override
	public TenantDTO updateTenant(TenantDTO tenant) {
		
		//Only SuperAdmin can update a tenant
		
		User currentUser = Utils.getCurrentUser();
		if (currentUser == null || !Utils.isSuperAdmin(currentUser.getRoles())) {
			throw new CustomGraphQLException(403, "Only SuperAdmin can update a tenant");
		}
		if (tenant.getId() == null) {
			throw new CustomGraphQLException(400, "Tenant ID is required for update");
		}
		Tenant existingTenant = tenantRepository.findById(tenant.getId())
				.orElseThrow(() -> new CustomGraphQLException(400, "Tenant with ID " + tenant.getId() + " does not exist"));
		
		existingTenant.setName(tenant.getName() != null ? tenant.getName() : existingTenant.getName());
		existingTenant.setMaxUsersAllowed(tenant.getMaxUsersAllowed() != 0 ? tenant.getMaxUsersAllowed() : existingTenant.getMaxUsersAllowed());
		existingTenant.setLicenseExpiry(tenant.getLicenseExpiry() != null ? tenant.getLicenseExpiry(): existingTenant.getLicenseExpiry());
		existingTenant.setUsers(tenant.getUsers() != null ? tenant.getUsers() : existingTenant.getUsers());
		Tenant updatedTenant = tenantRepository.save(existingTenant);		
		return TenantMapper.toDTO.apply(updatedTenant);
	}

	@Override
	public TenantDTO deleteTenant(Long id) {
		if (id == null) {
			throw new CustomGraphQLException(400, "Tenant ID is required for deletion");
		}
		Tenant tenant = tenantRepository.findById(id)
				.orElseThrow(() -> new CustomGraphQLException(400, "Tenant with ID " + id + " does not exist"));
		
		tenantRepository.delete(tenant);
		return TenantMapper.toDTO.apply(tenant);
	}

	@Override
	public PaginatedResponse<List<TenantDTO>> fetchTenants(int pageSize, int pageNumber, TenantSortField sortBy, SortDirection direction) {
	    try {
	    	//Only SuperAdmin can fetch all tenants
	    	User currentUser = Utils.getCurrentUser();
	    	if (currentUser == null || !Utils.isSuperAdmin(currentUser.getRoles())) {
	            throw new CustomGraphQLException(403, "Only SuperAdmin can fetch all tenants");
	        }
	        Sort sort = Sort.by(direction == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy.name());
	        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	        Page<Tenant> page = tenantRepository.findAll(pageable);
	        List<Tenant> tenants = page.getContent();
	        long totalCount = page.getTotalElements();
	        List<TenantDTO> list = tenants == null ? List.of() : tenants.stream().map(TenantMapper.toDTO::apply).toList();
	        return new PaginatedResponse<>(list, totalCount);
	    } catch (Exception e) {
	        // Log and return an empty response instead of null
	        // logger.error("Error fetching tenants", e);
	        return new PaginatedResponse<>(List.of(), 0L);
	    }
	}

}
