output "vpc_id" {
  value = aws_vpc.iac_module_vpc.id
}

output "stmw_sg" {
  value = aws_security_group.iac_module_sg.id
}

output "stmw_public_subnets" {
  value = aws_subnet.iac_module_public_subnets.*.id
}