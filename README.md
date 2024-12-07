# Kubernetes

## Introduction

This repository contains a sample Spring Boot application that uses MongoDB as its database, along with YAML files to deploy the application in a local Minikube cluster.

This repository includes all the necessary files to deploy the application on Minikube. The deployment is composed of several YAML files that create a MongoDB deployment, a service for the MongoDB database, a deployment for the Spring Boot application, and a service to expose the application.
## Prerequisites

- Docker
- minikube

## Getting Started
1. Clone repository:
```
git clone https://github.com/Anant1711/Task-3-Kubernetes.git
```
 
```gitignore
cd Task-3-Kubernetes
```

2. Build the Docker image and upload it to Docker Hub:

```
mvn install
docker build -t <your-username-of-dockerhub>/<your-repo-name-of-dockerhub> .
docker push <your-username>/<your-project>
```
3. Start Minikube:

```gitignore
minikube start
```

4. Now change directory to `/src/main/resources/` and apply `yaml files`:

```gitignore
kubectl apply -f mongo-config.yml
kubectl apply -f mongo-secret.yml
kubectl apply -f mongo-deployment.yml
kubectl apply -f deployment.yml
```
> NOTE: Follow the same order to run the command

5. Verify that the pods are running:
```gitignore
kubectl get pods
```
## YAML files

Here are the YAML files that you need to apply in the order listed above:

1. `mongo-secret.yml`: This file contains the MongoDB credentials. Be sure to replace the placeholder values with your own values.
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mongo-secret
data:
  username: <Enter_your_encoded_username_in_base64>
  password: <Enter_your_encoded_password_in_base64>
```

2. `mongo-config.yml`: This file creates a ConfigMap that contains the MongoDB configuration file.
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mongo-conf
data:
  host: mongodb-service
  database: <your_db_name>
```

3. `mongo-deployment.yml`: This file creates a deployment for the MongoDB database.
```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app: mongo
  name: mongodb-service
spec:
  ports:
    - port: 27017
      targetPort: 27017
  selector:
    app: mongo
  clusterIP: None  # We Use DNS, Thus ClusterIP is not relevant

---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mongo-pv-claim # name of PVC essential for identifying the storage data
  labels:
    app: mongo
    tier: database
spec:
  accessModes:
    - ReadWriteOnce   #This specifies the mode of the claim that we are trying to create.
  resources:
    requests:
      storage: 1Gi    #This will tell kubernetes about the amount of space we are trying to claim.
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mongo
  labels:
    app: mongo
spec:
  selector:
    matchLabels:
      app: mongo
  replicas: 1
  template:
    metadata:
      labels:
        app: mongo
      name: mongodb-service


    spec:
      containers:
        - image: mongo:latest
          name: mongo

          env:
            - name: MONGO_INITDB_ROOT_USERNAME
              valueFrom:
                secretKeyRef:
                  name: mongo-secret
                  key: username
            - name: MONGO_INITDB_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mongo-secret
                  key: password


          ports:
            - containerPort: 27017
              name: mongo
          volumeMounts:
            - name: mongo-persistent-storage
              mountPath: /data/db #This is the path in the container on which the mounting will take place.
      volumes:
        - name: mongo-persistent-storage # Obtaining 'volume' from PVC
          persistentVolumeClaim:
            claimName: mongo-pv-claim
```

4. `deployment.yml`: This file creates a deployment for your Spring Boot application and sets up a service to expose it.
```yaml
kind: Service
apiVersion: v1
metadata:
  name: spring-mongo-service
spec:
  selector:
    app: spring-mongo-service
  ports:
    - protocol: TCP
      port: 8080
      nodePort: 30081
  type: NodePort
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-mongo-service
spec:
  selector:
    matchLabels:
      app: spring-mongo-service
  replicas: 2
  template:
    metadata:
      labels:
        app: spring-mongo-service
    spec:
      containers:
        - name: spring-mongo-service
          image: <username_of_dockerhub>/<repo_name_of_dockerhub>
          #imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: MONGO_USERNAME
              valueFrom:
                secretKeyRef:
                  name: mongo-secret
                  key: username
            - name: MONGO_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mongo-secret
                  key: password
            - name: MONGO_DB
              valueFrom:
                configMapKeyRef:
                  name: mongo-conf
                  key: database
            - name: MONGO_HOST
              valueFrom:
                configMapKeyRef:
                  name: mongo-conf
                  key: host

```
