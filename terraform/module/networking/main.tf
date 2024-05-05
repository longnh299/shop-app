# vpc
resource "aws_vpc" "iac_module_vpc" {
  cidr_block = var.cidr_block_vpc
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = {
    Name = "stmw_vpc"
  }
}

# internet gateway
resource "aws_internet_gateway" "iac_module_igw" {
  vpc_id = aws_vpc.iac_module.id

  tags = {
    Name = "stmw_igw"
  }
}

# public subnet
resource "aws_subnet" "iac_module_public_subnets" {
  count = var.public_subnets_count
  vpc_id = aws_vpc.iac_module_vpc.id
  cidr_block = "10.29.${10 + count.index}.0/24"
  map_customer_owned_ip_on_launch = true

  tags = {
    Name = "stmw_public_subnet_${count.index + 1}"
  }
}

# route table
resource "aws_route_table" "iac_module_rtb" {
  vpc_id = aws_vpc.iac_module_vpc.id
  
  tags = {
    Name = "stmw_public_rtb"
  }
}

# route
resource "aws_route" "iac_module_rtb" {
  route_table_id = aws_route_table.iac_module_rtb.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id = aws_internet_gateway.iac_module_igw.id
}

# association rtb and subnet
resource "aws_route_table_association" "iac_module_rtb" {
  subnet_id = aws_subnet.iac_module_public_subnets.id
  route_table_id = aws_route_table.iac_module_rtb.id
  count = var.public_subnets_count

}

locals {

  port_in_22 = [
    22
  ]

  port_in_80 = [
    80
  ]

  port_in_443 = [
    443
  ]

  port_in_8080 = [
    8080
  ]

  port_in_9100 = [
    9100
  ]

}

# security group
resource "aws_security_group" "iac_module_sg" {
  name = "stmw_sg"
  vpc_id = aws_vpc.iac_module_vpc.id

  dynamic "ingress" {
    for_each = toset(local.port_in_22)
    content {
      from_port        = ingress.value
      to_port          = ingress.value
      protocol         = "tcp"
      cidr_blocks = "0.0.0.0/0"
      }
  }

  dynamic "ingress" {
    for_each = toset(local.port_in_80)
    content {
      from_port        = ingress.value
      to_port          = ingress.value
      protocol         = "tcp"
      cidr_blocks = "0.0.0.0/0"
      }
  }

  dynamic "ingress" {
    for_each = toset(local.port_in_443)
    content {
      from_port        = ingress.value
      to_port          = ingress.value
      protocol         = "tcp"
      cidr_blocks = "0.0.0.0/0"
      }
  }

  dynamic "ingress" {
    for_each = toset(local.port_in_8080)
    content {
      from_port        = ingress.value
      to_port          = ingress.value
      protocol         = "tcp"
      cidr_blocks = "0.0.0.0/0"
      }
  }

  dynamic "ingress" {
    for_each = toset(local.port_in_9100)
    content {
      from_port        = ingress.value
      to_port          = ingress.value
      protocol         = "tcp"
      cidr_blocks = "0.0.0.0/0"
      }
  }

  # inbound for ping 
  ingress = {
    from_port = -1
    to_port = -1
    protocol = "icmp"
  }

  egress = {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_block = ["0.0.0.0/0"]
  }

}

