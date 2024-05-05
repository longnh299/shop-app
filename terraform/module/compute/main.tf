
data "aws_ami" "amzn-linux-2023-ami" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023.*-x86_64"]
  }

  filter {
    name   = "root-device-type"
    values = ["ebs"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

}

resource "aws_launch_template" "ec2_launch_template" {
  name = "stmw_launch_template"
  instance_type = var.instance_type
  image_id = data.aws_ami.amzn-linux-2023-ami.id
  vpc_security_group_ids = [ var.worker_sg ] # get from networking module
  key_name = var.ssh_key # worker.pem
  
  block_device_mappings {
    device_name = "/dev/sda1"

    ebs {
      volume_size = 25
    }
  }

}

resource "aws_instance" "server_intances" {
  count = var.aws_instance_count
  subnet_id = var.public_subnet_id
  associate_public_ip_address = true

  network_interface {
    network_interface_id = var.stmw_eni # get from networking module
    device_index         = 0
  }

  tags = {
    Name = "wokerr${count.index + 1}"
  }
}