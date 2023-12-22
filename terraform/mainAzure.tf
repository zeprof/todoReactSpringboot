# Azure provider version

terraform {
  required_providers {
    azurerm = {
      source = "hashicorp/azurerm"
      version = "= 3.21.1"
    }
  }
}

provider "azurerm" {
    features {}
}

### Create Resource group
resource "azurerm_resource_group" "todo_app_rg" {
    name      = var.resource_group_name
    location  = var.location
}

resource "azurerm_spring_cloud_service" "todo_app_rg" {
  name                = "todoapp-springcloud"
  resource_group_name = azurerm_resource_group.todo_app_rg.name
  location            = azurerm_resource_group.todo_app_rg.location
  sku_name            = "S0"

}

resource "azurerm_spring_cloud_app" "todo_app_rg" {
  name                = "todoapp"
  resource_group_name = azurerm_resource_group.todo_app_rg.name
  service_name        = azurerm_spring_cloud_service.todo_app_rg.name
  is_public           = true
  https_only          = true

  identity {
    type = "SystemAssigned"
  }
}

resource "azurerm_spring_cloud_java_deployment" "todo_app_rg" {
  name                = "default"
  spring_cloud_app_id = azurerm_spring_cloud_app.todo_app_rg.id
  instance_count      = 1
  jvm_options         = "-Dspring.profile.active=azure"
  runtime_version     = "Java_17"

  environment_variables = {
    "Env" : "Staging"
  }
}

resource "azurerm_spring_cloud_active_deployment" "todo_app_rg" {
  spring_cloud_app_id = azurerm_spring_cloud_app.todo_app_rg.id
  deployment_name     = azurerm_spring_cloud_java_deployment.todo_app_rg.name
}