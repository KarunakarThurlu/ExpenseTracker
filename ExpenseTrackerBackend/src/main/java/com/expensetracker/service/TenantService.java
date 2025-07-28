package com.expensetracker.service;

import java.util.List;

import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.TenantDTO;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.TenantSortField;

public interface TenantService {
	TenantDTO saveTenant(TenantDTO tenant);
	TenantDTO fetchTenant(Long id);
	TenantDTO updateTenant(TenantDTO tenant);
	TenantDTO deleteTenant(Long id);
	PaginatedResponse<List<TenantDTO>> fetchTenants(int pageSize, int pageNumber, TenantSortField sortBy, SortDirection direction);
}
