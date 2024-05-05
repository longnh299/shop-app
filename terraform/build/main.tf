provider "aws" {
  region = local.location
}

locals {
  instance_type = "t2.micro"
  location = "ap-southeast-1"
  evn = "dev"
  vpc_cidr = "10.29.0.0/16"
}

module "networking" {
  source = "../module/networking"
  public_subnets_count = 2
  cidr_block_vpc = local.vpc_cidr
}

module "compute" {
  source = "../module/compute"
  worker_sg = module.networking.stmw_sg
  ssh_key = "worker"
  aws_instance_count = 2
  instance_type = local.instance_type
  stmw_eni = module.networking.stmw_eni
  public_subnet_id = module.networking.stmw_public_subnets

}