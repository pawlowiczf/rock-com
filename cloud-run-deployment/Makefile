deploy_backend:
	gcloud run deploy backend-service \
	  --image=europe-central2-docker.pkg.dev/kubernetes-rockcom/rockcom/backend:latest \
	  --platform=managed \
	  --region=europe-central2 \
	  --allow-unauthenticated \
	  --service-account=kubernetes-rockcom@kubernetes-rockcom.iam.gserviceaccount.com \
	  --set-secrets=\
	SPRING_DATASOURCE_URL=SPRING_DATASOURCE_URL:latest,\
	SPRING_DATASOURCE_USERNAME=SPRING_DATASOURCE_USERNAME:latest,\
	SPRING_DATASOURCE_PASSWORD=SPRING_DATASOURCE_PASSWORD:latest

deploy_frontend:
	gcloud run deploy frontend-service \
	  --image=europe-central2-docker.pkg.dev/kubernetes-rockcom/rockcom/frontend:latest \
	  --platform=managed \
	  --region=europe-central2 \
	  --service-account=kubernetes-rockcom@kubernetes-rockcom.iam.gserviceaccount.com \
	  --allow-unauthenticated


push_frontend_image:
	docker build -t europe-central2-docker.pkg.dev/kubernetes-rockcom/rockcom/frontend -f ../frontend/DockerfileCloudRun ../frontend/.
	docker push europe-central2-docker.pkg.dev/kubernetes-rockcom/rockcom/frontend

push_backend_image:
	docker build -t europe-central2-docker.pkg.dev/kubernetes-rockcom/rockcom/backend -f ../backend/DockerfileCloudRun ../backend/.
	docker push europe-central2-docker.pkg.dev/kubernetes-rockcom/rockcom/backend