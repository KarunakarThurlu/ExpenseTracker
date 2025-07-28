package com.expensetracker.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.TenantDTO;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.TenantSortField;
import com.expensetracker.service.TenantService;

import jakarta.validation.Valid;

@Controller
public class TenantController {
	
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TenantController.class);

	private final TenantService tenantService;

	public TenantController(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	@MutationMapping
	public TenantDTO saveTenant(@Valid @Argument("tenant") TenantDTO tenantDTO) {
		return tenantService.saveTenant(tenantDTO);
	}

	@MutationMapping
	public TenantDTO updateTenant(@Valid @Argument("tenant")TenantDTO tenant) {
		return tenantService.updateTenant(tenant);
	}

	@MutationMapping
	public TenantDTO deleteTenant(@Argument("id")Long id) {
		return tenantService.deleteTenant(id);
	}

	@QueryMapping
	public TenantDTO fetchTenant(@Argument("id")Long id) {
		return tenantService.fetchTenant(id);
	}

	@QueryMapping
	public PaginatedResponse<List<TenantDTO>> tenants(
			@Argument("pageSize") int pageSize,
			@Argument("pageNumber") int pageNumber,
			@Argument("sortBy") String sortBy,
			@Argument("sortDirection") String sortDirection) {
		logger.info("Fetching tenants with pageSize: {}, pageNumber: {}, sortBy: {}, sortDirection: {}", 
				pageSize, pageNumber, sortBy, sortDirection);
		return tenantService.fetchTenants(pageSize, pageNumber, TenantSortField.valueOf(sortBy), SortDirection.valueOf(sortDirection));
	}

}
