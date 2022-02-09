# treevolution

To build in docker
Install docker (Ubuntu steps)
> sudo apt install docker.io
> 
> sudo systemctl start docker
> 
> sudo systemctl enable docker

Verify image
> docker --version

Create the image
> docker build -t treevolution . 

Run the image
> docker run treevolution
