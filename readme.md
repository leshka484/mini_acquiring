1. docker exec -it mini-acquiring-db psql -U postgres -d postgres
    
2. CREATE ROLE ma_admin
   WITH LOGIN
   PASSWORD 'ma_admin'
   NOSUPERUSER
   NOCREATEDB
   NOCREATEROLE
   NOREPLICATION;

3. ALTER DATABASE mini_acquiring OWNER TO ma_admin;