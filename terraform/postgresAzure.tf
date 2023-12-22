
resource "azurerm_postgresql_flexible_server" "todo-postgres-server" {
  name                   = "todo-postgres-server"
  resource_group_name    = azurerm_resource_group.todo_app_rg.name
  location               = azurerm_resource_group.todo_app_rg.location
  version                = "14"
  administrator_login    = "lacouf"
  administrator_password = "Patate123"
  storage_mb             = 32768
  sku_name               = "B_Standard_B1ms"

}