removeAll:
	kubectl delete deployment rockcom-backend
	kubectl delete service rockcom-backend-service
	kubectl delete deployment rockcom-frontend
	kubectl delete service rockcom-frontend-service
#	kubectl delete configmap nginx-config
#	kubectl delete deployment nginx
#	kubectl delete service nginx

runAll:
	kubectl apply -f backend-deployment.yaml
	kubectl apply -f backend-service.yaml
	kubectl apply -f frontend-deployment.yaml
	kubectl apply -f frontend-service.yaml
	kubectl apply -f nginx-configmap.yaml
	kubectl apply -f nginx-deployment.yaml
	kubectl apply -f nginx-service.yaml