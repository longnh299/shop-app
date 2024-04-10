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

## III. Deploy result
### 1. Run ansible-playbook to set up monitor and worker machines

- Run successfully
  
  <div align="center">
    <img width="1500" src="assets/deploy-all-successfully.png" alt="deploy-sucessfully">
  </div>

<div align="center">
  <i>Result after run ansible-playbook</i>
</div>

- Check image on Dockerhub

  <div align="center">
    <img width="1500" src="assets/dockerhub.png" alt="dockerhub">
  </div>

<div align="center">
  <i>Backend image and Frontend image were pushed to Dockerhub</i>
</div>

- Test Webapp deployment on AWS EC2
  
  <div align="center">
    <img width="1500" src="assets/deploy-on-ec2-using-ansible.png" alt="webapp-ec2">
  </div>

<div align="center">
  <i>Web app is hosted at http://ec2-18-138-227-65.ap-southeast-1.compute.amazonaws.com:9333/</i>
</div>

- Check Prometheus targets
  
  <div align="center">
    <img width="1500" src="assets/list_job_target.png" alt="prometheus-target">
  </div>

<div align="center">
  <i>All targets are upped</i>
</div>

- Add Prometheus data source in Grafana

  <div align="center">
    <img width="1500" src="assets/assign-prometheus-datasource-grafana.png" alt="prometheus-datasource">
  </div>

<div align="center">
  <i>Assign data source in Grafana</i>
</div>

- Grafana dashboards

<div align="center">
  <img width="1500" src="assets/localhost-grafana.png" alt="local-dashboard">
</div>

<div align="center">
  <i>Monitor machine dashboard</i>
</div>

<div align="center">
  <img width="1500" src="assets/worker1-grafana.png" alt="worker1-dashboard">
</div>

<div align="center">
  <i>Worker1 dashboard</i>
</div>

<div align="center">
  <img width="1500" src="assets/worker2-grafana.png" alt="worker2-dashboard">
</div>

<div align="center">
  <i>Worker2 dashboard</i>
</div>

### 2. Alertmanager send alerts to Telegram, Slack, Gmail

- When two worker have disconnected, status at Prometheus targets is Down

<div align="center">
  <img width="1500" src="assets/turn-off-wifi-to-make-test.png" alt="worker-down">
</div>

<div align="center">
  <i>Workers are downed</i>
</div>

- Alerts ares pushed to Alertmanager from Prometheus
  
<div align="center">
  <img width="1500" src="assets/alert-manager-push-alert.png" alt="alert-manager">
</div>

<div align="center">
  <i>Alerts about PrometheusTargetsMissing</i>
</div>

- Alerts are sent to Telegram
  
<div align="center">
  <img width="1500" src="assets/alert-pushed-to-telegram.png" alt="alert-telegram">
</div>

- Alerts are sent to Slack

<div align="center">
  <img width="1500" src="assets/alert-pushed-to-slack.png" alt="alert-slack">
</div>

- Alerts are send to Gmail

<div align="center">
  <img width="1500" src="assets/alert-pushed-to-gmail.png" alt="alert-gmail">
</div>

## IV. Software version

|  Software |  Version |
|---|---|
| Ansible | 2.12.10 |
| Docker | 26.0.0 |
| Docker Compose | 1.25.0 |
| VMware Workstation Player | 17 |


  



  
  

  
