FROM node:alpine

MAINTAINER longnh09

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY . .

# install angular cli 
RUN npm install -g @angular/cli@16.1.0

# install project dependecied
RUN npm install

EXPOSE 4200

ENTRYPOINT ["npm","start"]