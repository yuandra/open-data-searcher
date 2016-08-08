# Open Data Portal Searcher

The aim for this app is to create a simple and lightweight interface that can easily crawl and search all open data portals in the world.
In doing so, it aim to help people to easily find the data that they wanted from various open data portals.

## Currently supported open data portals :
- CKAN

## Architecture
- Play Framework
- Jackan

## Installation

### ACTIVATOR
- Get play framework from [https://www.playframework.com/](https://www.playframework.com)

### Heroku

- [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

## Usage

- Set up your own list of data portals that you want to crawl by editing public/data/open_data_portal.csv
- Set up APPLICATION_SECRET in conf/application.conf

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## TO-DO List

- Implement paging for search result
- Optimise export to CSV
- Better search options
- Support more type of open data portals

## History

2016/08/09 - Initial Commit

## Credits

This project is supported by the [School of Data](http://schoolofdata.org)

## License

GPL
