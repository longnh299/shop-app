# Container tạm thời để build jar file bằng Gradle  , cách dùng multiple state image này giúp cho giảm tải dung lượng cho container chính 
# khi chạy (không cần phải cài gradle)
# vi du trong project nay neu khong dung multiple state thi image size la 950mb, khi dung multi state image size la 353mb
FROM gradle:8.7.0-jdk17  AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

# Copy project src to temp container
COPY . . 
RUN gradle build


# main container
FROM amazoncorretto:17-alpine-jdk

LABEL authors="longnh09"

ENV ARTIFACT_NAME=ShopBE-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

# Copy file JAR từ container tạm thời
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

# copy images folder to container
COPY /uploads /usr/app/uploads

EXPOSE 8080

# start app when container start
ENTRYPOINT ["java","-jar","ShopBE-0.0.1-SNAPSHOT.jar"]