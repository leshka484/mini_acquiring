# Инициализация базы данных

Убедитесь, что контейнер с БД запущен.

Для создания роли владельца базы данных и назначения ему прав владельца БД выполните команду из корневой директории проекта:

```bash
docker exec -i mini-acquiring-db psql -U postgres -d postgres < ./src/main/resources/sql/create_db_owner_role.sql
```