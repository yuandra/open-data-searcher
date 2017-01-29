# Open Data Portal Searcher

This is web application that allows everyone to create and run their own open data portal searcher easily by using Google Sheets and Heroku.  
  
There is a lot of open data portals in the world and if we want to look for data, we have to go to those sites one by one and look individually.  
This application aims to create a simple and lightweight interface that can search all open data portals using their APIs.  
In doing so, it want to help encourage people in learning how to work with data by enabling them to easily find the data that they wanted from various open data portals.  

Five steps to use the app :

- Make a copy of the app code via Github, a free platform for hosting open source code.
- Make a copy of the configuration Google Sheets and put their own list of data portals there, including their preferred language
- Deploy and run the app in just one click by leveraging Heroku’s free app tier
- Copy and paste the link of the Google Sheets to the app
- Voilà ! You have your own open data portal searcher up and running :)

## Credits

This project is supported by the [School of Data](http://schoolofdata.org) Member Support Programme and Coordination Team
![SchoolOfDataLogo](public/images/logo_scoda_new.png?raw=true)  

## Currently supported open data portals :
- CKAN

## Usage

You only need set up two things to easily set up your own open data searcher website :
- Set up your configurations using a Google Sheet to manage which data portals to search and languages of the open data searcher website.
- Run your website on Heroku (a service where you can run web based applications) by clicking on the Heroku button below. You will need to create a free account and you can just use the free tier version of Heroku (no need to pay anything) for trial & prototyping. If you want to deploy it full time then you can switch to the paid tier. 

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)


How to set up the configuration :

- Open the sample spreadsheet [here](https://docs.google.com/spreadsheets/d/1zNaURLb8kMeia0T5ywGPPAfrvQuaq6qgGpX1VNdzBGE)  

- Make a copy of the spreadsheet   

![Config1](public/images/config1.png?raw=true)  

- There are 3 sheets for you to change. Sheet 1 is data portal sheet which shows what data portal that the tool are able to crawl. Note that only CKAN data portal is supported now. Sheet 2 is the config sheets that allow you put configuration like what language to use and how many data to search. Sheet 3 is the translation sheet for you to put translation of the tool's text in different languages. Modify the settings as you wish  

- Click on the publish spreadsheet option   

![Config2](public/images/config2.png?raw=true)  

- Set the option to publish entire document and set the format as Comma Separated Values (CSV) files. Copy and take note of the generated link. Put the link in the base URL field   

![Config3](public/images/config3.png?raw=true)  

- We will need to put an identifier for each of the sheet. The identifier will be shown as the number after the 'gid=' in the link when you click on each sheet. Example in the image is the gid for the sheet data portal (and usually the first sheet is 0) Copy the gid (only the number) for the data portal sheet field  

![Config5](public/images/config4.png?raw=true)  

- Get the gid for the config sheet. Put the gid number in the config sheet field   

![Config5](public/images/config5.png?raw=true)  

- Get the gid for the translation sheet. Put the gid number in the translation sheet field  

- Click submit  

- Congratulations ! Your tool is now ready to use  

## Contributing

### Architecture

- Play Framework
- Bootstrap
- Jackan

### Requirements  

- Get play framework from [https://www.playframework.com/](https://www.playframework.com)

### Deployment

- [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

### Making Changes

Submit your contributions such as bug fixes, feature updates, and adding new parsers for other open data portals.

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Future Ideas

1. Parsers for other types of open data portals or APIs (Note for parsers will need to output a SearchResult that has a Dataset object. See more in the [GeneralClass.scala](blob/master/app/model/GeneralClass.scala))
2. Connection to databases and paging system
3. Basic visualisation or analytics

## History

- 2016/08/09 - 0.1 - Initial Commit
- 2016/10/06 - 0.2 - switching configurations to Google Sheets, add multiple language translation and modularity

## License

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.  
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the [GNU General Public License](http://www.gnu.org/licenses/) for more details.
