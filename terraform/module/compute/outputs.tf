output "worker_dns" {
  value = aws_instance.server_intances.*.public_dns
}

output "worker_public_ip" {
  value = aws_instance.server_intances.*.public_ip
}