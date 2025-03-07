CREATE TABLE order_product (
                               order_id BIGINT NOT NULL,
                               product_id BIGINT NOT NULL,
                               PRIMARY KEY (order_id, product_id),
                               CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES "order" (id),
                               CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id)
);