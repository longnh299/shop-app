# Deploy Prometheus + Alertmanager + Grafana stack to monitor server using Ansible

## I. Requirements

1. Deploy Prometheus-Grafana-Alertmanager stack and Web application
   * Using docker, docker-compose, ansible to deploy
2. Define alert rules to monitoring  target hosts
3. Configure Alertmanager to push alert to Telegram, Slack, Mail...
4. Create Grafana dashboards to monitoring target hosts

## II. System Architecture

### 1. Overview
   * 2 machines on AWS to deploy web application.
   * 1 machine on VMWare to deploy monitoring system.
   * Using Ansible to deploy web app, monitoring system, package... on 3 above machines.
     
### 2. Monitor machine
I will create a monitor machine on Vmware Workstation 17 Player (Ubuntu 20.04), then I deploy Prometheus-Grafana-Alertmanager stack using docker, docker-compose and Ansible on this machine

### 3. Worker machines
I will create 2 EC2 instances (Ubuntu 20.04) on AWS for worker nodes
- Worker1 for deploy web application and Node exporter
  - Web app: http://ec2-18-138-227-65.ap-southeast-1.compute.amazonaws.com:9333
  - Node exporter: http://ec2-18-138-227-65.ap-southeast-1.compute.amazonaws.com:9100
    
- Worker2 for only Node exporter
  - Node exporter: http://ec2-47-128-74-122.ap-southeast-1.compute.amazonaws.com:9100

I use Ansible to deploy web app and node exporter on 2 ec2 worker. Because I will need to get the node_exporter metrics from port 9100, so I need to change the Inbound rules of security group to allow access our EC2 instances in port 9100.

### 4. Diagram

<div align="center">
  <img width="1500" src="assets/diagram.png" alt="Diagram">
</div>

<div align="center">
  <i>Deployment diagram.</i>
</div>
