FROM nginx:1.27.2-alpine3.20
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY ./dist/onboarding-frontend/browser /usr/share/nginx/html
EXPOSE 80
