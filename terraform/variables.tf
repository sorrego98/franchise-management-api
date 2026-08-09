variable "mongodbatlas_project_id" {
  description = "MongoDB Atlas Project Id"
  type        = string
}

variable "mongodbatlas_username" {
  description = "MongoDB Atlas Username"
  type = string
}

variable "mongodbatlas_password" {
  description = "MongoDB Atlas Password"
  type = string
  sensitive = true
}