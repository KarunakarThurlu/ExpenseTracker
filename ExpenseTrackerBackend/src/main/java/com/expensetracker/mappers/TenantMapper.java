package com.expensetracker.mappers;

import java.util.function.Function;

import com.expensetracker.dto.TenantDTO;
import com.expensetracker.entity.Tenant;

public class TenantMapper {
	
	 public static final Function<Tenant, TenantDTO> toDTO = tenant -> {
		 TenantDTO tenantDTO = new TenantDTO();
		 tenantDTO.setId(tenant.getId());
		 tenantDTO.setName(tenant.getName());
		 tenantDTO.setMaxUsersAllowed(tenant.getMaxUsersAllowed());
		 tenantDTO.setLicenseExpiry(tenant.getLicenseExpiry());
		 tenantDTO.setCreatedAt(tenant.getCreatedAt());
		 tenantDTO.setUpdatedAt(tenant.getUpdatedAt());
		 tenantDTO.setUsers(tenant.getUsers());
		 return tenantDTO;
	 };
	 
	 public static final Function<TenantDTO, Tenant> toEntity = tenantDTO -> {
		 Tenant tenant = new Tenant();
		 tenant.setName(tenantDTO.getName());
		 tenant.setMaxUsersAllowed(tenantDTO.getMaxUsersAllowed());
		 tenant.setLicenseExpiry(tenantDTO.getLicenseExpiry());
		 tenant.setUsers(tenantDTO.getUsers());
		 return tenant;
	 };
}
