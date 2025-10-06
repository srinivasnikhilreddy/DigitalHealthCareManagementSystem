package com.iss.services;

import com.iss.models.AdminSettings;
import com.iss.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService
{
    @Autowired
    private AdminRepository adminRepository;

    public AdminSettings saveSetting(String key, String value)
    {
        return adminRepository.save(AdminSettings.builder().key(key).value(value).build());
    }

    public AdminSettings getSetting(String key)
    {
        return adminRepository.findByKey(key);
    }
}
