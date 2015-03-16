# Installing the oltpbenchadmin #

The oltpbenchadmin's tool has two software components: (i)  oltpbenchmark-proxy is a proxy that interacts with the oltpbenchmark
(ii) oltpbenchmark is a graphical interface to control the experiment's environment.

## Installing oltpbenchadmin-proxy (the proxy) ##

  1. Download the file [oltpbenchadmin-proxy.zip](http://oltpbenchadmin.googlecode.com/files/oltpbenchadmin-proxy_v0.1.zip)
  1. Unzip the file [oltpbenchadmin-proxy.zip](http://oltpbenchadmin.googlecode.com/files/oltpbenchadmin-proxy_v0.1.zip) preserving their directory structure
  1. Open your terminal command line and enter the root directory of the unzipped file
  1. Run the file **oltpbechmark-proxy.sh** to install the required components. To run the file **oltpbenchadmin-proxy.sh**, you must use the following command syntax `./oltpbenchmark-proxy.sh <root directory of the oltpbenchmark>`
    * Do not forget to check if the root directory of the oltpbenchmark has write permission and that the file **oltpbenchmark-proxy.sh** has execute permission
  1. Use the command `java -jar oltpbenchadmin-proxy.jar <port>`
  1. Edit the generated file **oltpbenchadmin-proxy.xml** in the root directory and add the database user/password
  1. Finally, to start the proxy, use the command `java -jar oltpbenchadmin-proxy.jar <port>`

## Installing oltpbenchadmin (the graphical interface) ##

  1. Download the file [oltpbenchadmin.zip](http://oltpbenchadmin.googlecode.com/files/oltpbenchadmin_v0.1.zip)
  1. Unzip the file [oltpbenchadmin.zip](http://oltpbenchadmin.googlecode.com/files/oltpbenchadmin_v0.1.zip) preserving their directory structure
  1. Open your terminal command line and enter the root directory of the unzipped file
  1. Use the command `java -jar oltpbenchmark.jar` to run the graphical interface of the oltpbenchadmin