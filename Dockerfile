FROM eclipse-temurin:25-jre-alpine

LABEL org.opencontainers.image.source="https://github.com/Vempain/vempain-mcp"

EXPOSE 8080 8081

RUN apk add --no-cache curl su-exec \
    && mkdir -p /vempain_mcp \
    && adduser -D -h /vempain_mcp/vempain -u 6666 -H vempain

ADD service/build/libs/vempain-mcp-*.jar /app.jar
COPY container-entrypoint.sh /usr/local/bin/container-entrypoint.sh
RUN chmod 755 /usr/local/bin/container-entrypoint.sh

ENTRYPOINT ["/usr/local/bin/container-entrypoint.sh"]
CMD ["-jar", "/app.jar"]
