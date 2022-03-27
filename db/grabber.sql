create table if not exists post (
     id serial primary key,
     title text,
     discription text,
     link text unique,
     created timestamp
);