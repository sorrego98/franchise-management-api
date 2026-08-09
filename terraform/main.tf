data "mongodbatlas_project" "this" {
  project_id = var.mongodbatlas_project_id
}

resource "mongodbatlas_cluster" "franchise" {
  project_id = var.mongodbatlas_project_id
  name = "franchise-cluster"
  provider_name = "TENANT"
  provider_region_name = "US_EAST_1"
  provider_instance_size_name = "M0"

}

resource "mongodbatlas_database_user" "franchise" {
  auth_database_name = "admin"
  project_id = var.mongodbatlas_project_id
  username = var.mongodbatlas_username
  password = var.mongodbatlas_password

  roles {
    database_name = "franchise_db"
    role_name     = "readWrite"
  }
}

resource "mongodbatlas_project_ip_access_list" "franchise" {
  project_id = var.mongodbatlas_project_id
  cidr_block = "181.133.43.182/32"
  comment = "local deveploment"
}