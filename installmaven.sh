yum install -y wget
yum install -y yum-utils

yum-config-manager --add-repo http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo

yum-config-manager --enable epel-apache-maven

yum install -y apache-maven
