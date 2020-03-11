# see: https://www.prestonlamb.com/blog/creating-a-docker-image-with-github-actions 
echo "Building docker images ..."
docker build -t hantsy/jakartaee8-starter-payara -f ./Dockerfile.payara .
docker build -t hantsy/jakartaee8-starter-wildfly -f ./Dockerfile.wildfly .  
docker build -t hantsy/jakartaee8-starter-ol -f ./Dockerfile.openliberty .  

echo "Pushing docker images ..."  
docker push hantsy/jakartaee8-starter-payara 
docker push hantsy/jakartaee8-starter-wildfly
docker push hantsy/jakartaee8-starter-ol

echo "Done"
