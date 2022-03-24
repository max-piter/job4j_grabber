create table if not exists post (
     id serial primary key,
     n_name text,
     t_text text,
     link text unique,
     created_date timestamp
);