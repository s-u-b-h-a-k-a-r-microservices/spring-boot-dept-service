package org.subhakar.services.department.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.subhakar.services.department.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, String> {
	List<Department> findByOrganizationId(Long organizationId);
}
