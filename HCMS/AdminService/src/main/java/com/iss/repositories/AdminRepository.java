package com.iss.repositories;

import com.iss.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminSettings, Long>
{
    AdminSettings findByKey(String key);
}
