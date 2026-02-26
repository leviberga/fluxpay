CREATE TABLE transactions (
transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
amount DECIMAL(19, 2) NOT NULL,
sender_id UUID NOT NULL REFERENCES users(user_id),
receiver_id UUID NOT NULL REFERENCES users(user_id),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT sender_not_receiver CHECK (sender_id <> receiver_id)
)