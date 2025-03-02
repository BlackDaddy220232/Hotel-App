INSERT INTO hotels (name, description, brand, house_number, street, city, country, post_code, phone, email, check_in, check_out)
VALUES
    ('DoubleTree by Hilton Minsk', 'A luxurious hotel in Minsk with stunning views.', 'Hilton', 9, 'Pobediteley Avenue', 'Minsk', 'Belarus', '220004', '+375 17 309-80-00', 'info@hiltonminsk.com', '14:00', '12:00'),

    ('Marriott Moscow', '5-star luxury hotel near Red Square.', 'Marriott', 4, 'Tverskaya Street', 'Moscow', 'Russia', '125009', '+7 495 937-00-00', 'contact@marriott-moscow.com', '15:00', '11:00'),

    ('The Ritz-Carlton Berlin', 'A prestigious hotel with top-class amenities.', 'Ritz-Carlton', 3, 'Potsdamer Platz', 'Berlin', 'Germany', '10785', '+49 30 337777', 'berlin@ritzcarlton.com', '13:00', '12:00'),

    ('Shangri-La Paris', 'A palace hotel with an exclusive view of the Eiffel Tower.', 'Shangri-La', 10, 'Avenue dIéna', 'Paris', 'France', '75116', '+33 1 5367-1999', 'paris@shangri-la.com', '16:00', '12:00');


INSERT INTO hotel_amenities (hotel_id, amenity) VALUES
(1, 'Free Wi-Fi'), (1, 'Spa'), (1, 'Gym'), (1, '24h Room Service'),
(2, 'Swimming Pool'), (2, 'Free Parking'), (2, 'Business Center'), (2, 'Bar'),
(3, 'Pet Friendly'), (3, 'Rooftop Bar'), (3, 'Concierge Service'), (3, 'Sauna'),
(4, 'Gourmet Restaurant'), (4, 'Valet Parking'), (4, 'Luxury Spa'), (4, 'Conference Rooms');
