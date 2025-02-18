CREATE TABLE parking_centers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    contact VARCHAR(255)
);

CREATE TABLE parking_spots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spot_code VARCHAR(10) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    parking_center_id BIGINT,
    FOREIGN KEY (parking_center_id) REFERENCES parking_centers(id)
); 