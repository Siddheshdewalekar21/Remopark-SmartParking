package com.remopark.config;

import com.remopark.model.ParkingCenter;
import com.remopark.model.Role;
import com.remopark.model.RoleType;
import com.remopark.model.User;
import com.remopark.repository.ParkingCenterRepository;
import com.remopark.repository.RoleRepository;
import com.remopark.repository.UserRepository;
import com.remopark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ParkingCenterRepository parkingCenterRepository;

    @Override
    public void run(String... args) {
        try {
            // Initialize roles if they don't exist
            if (roleRepository.count() == 0) {
                Role userRole = new Role(RoleType.ROLE_USER);
                Role adminRole = new Role(RoleType.ROLE_ADMIN);
                Role staffRole = new Role(RoleType.ROLE_STAFF);

                roleRepository.save(userRole);
                roleRepository.save(adminRole);
                roleRepository.save(staffRole);

                // Create admin user
                if (!userRepository.existsByUsername("admin")) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode("admin"));
                    admin.setEmail("admin@remopark.com");
                    admin.setEnabled(true);

                    Set<Role> roles = new HashSet<>();
                    roles.add(adminRole);
                    admin.setRoles(roles);

                    userRepository.save(admin);
                }
            }

            // Initialize parking centers
            initializeParkingCenters();

        } catch (Exception e) {
            // Log the error but don't prevent application startup
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeParkingCenters() {
        if (parkingCenterRepository.count() == 0) {
            // Create Central Parking
            ParkingCenter center1 = new ParkingCenter();
            center1.setName("Central Parking");
            center1.setPrice(50);
            center1.setLatitude(18.5204);
            center1.setLongitude(73.8567);
            center1.setContact("123-456-7890");
            parkingCenterRepository.save(center1);

            // Create West End Parking
            ParkingCenter center2 = new ParkingCenter();
            center2.setName("West End Parking");
            center2.setPrice(40);
            center2.setLatitude(18.5304);
            center2.setLongitude(73.8467);
            center2.setContact("123-456-7891");
            parkingCenterRepository.save(center2);
        }
    }
}