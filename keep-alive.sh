mkdir keep-alive
cd keep-alive
wget http://lolin1-data-provider-eu1.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu2.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu3.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu4.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu5.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu6.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu7.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu8.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-eu9.herokuapp.com/services/champions/list?realm=na&locale=en_US
wget http://lolin1-data-provider-usa1.herokuapp.com/services/champions/list?realm=na&locale=en_US
cd ..
rm -rf keep-alive