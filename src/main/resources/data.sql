-- 사용자
INSERT INTO hanghae_users (id, name, password)
VALUES ('user1', '사용자1', '1234'),
       ('user2', '사용자2', '1234'),
       ('user3', '사용자3', '1234'),
       ('user4', '사용자4', '1234'),
       ('user5', '사용자5', '1234'),
       ('user6', '사용자6', '1234'),
       ('user7', '사용자7', '1234'),
       ('user8', '사용자8', '1234'),
       ('user9', '사용자9', '1234'),
       ('user10', '사용자10', '1234');

-- 포인트
INSERT INTO hanghae_points (amount, id, user_id)
VALUES (100000, 1, 'user1'),
       (100000, 2, 'user2'),
       (100000, 3, 'user3'),
       (100000, 4, 'user4'),
       (100000, 5, 'user5'),
       (100000, 6, 'user6'),
       (100000, 7, 'user7'),
       (100000, 8, 'user8'),
       (100000, 9, 'user9'),
       (100000, 10, 'user10');

-- 상품
INSERT INTO hanghae_products (id, price, name)
VALUES (1, 90, '상품1'),
       (2, 70, '상품2'),
       (3, 50, '상품3'),
       (4, 60, '상품4'),
       (5, 80, '상품5');

-- 상품 재고
INSERT INTO hanghae_product_inventories (amount, id, product_id)
VALUES (50, 1, 1),
       (30, 2, 2),
       (70, 3, 3),
       (40, 4, 4),
       (90, 5, 5);