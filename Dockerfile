FROM ghcr.io/graalvm/native-image-community:21
WORKDIR /app

RUN microdnf install findutils

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

COPY src src
RUN ./gradlew nativeCompile --no-daemon

FROM debian:bookworm-slim
WORKDIR /app

COPY --from=0 /app/build/native/nativeCompile/my-app-name .

EXPOSE 8080
ENTRYPOINT ["./my-app-name"]