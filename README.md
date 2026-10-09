# COVID-19 Tracker
Simple Spring Boot-based application to track global COVID-19 cases

Heroku-based deploy running at http://covid19-tracker.herokuapp.com/

Data pulled from [Johns Hopkins CSSEGISandData](https://github.com/CSSEGISandData) repository 

Based off of javabrains.io sample

## Running locally

```
./mvnw spring-boot:run
```

Then open http://localhost:8080/. Run the tests with `./mvnw test`.

## Configuration

Set in `src/main/resources/application.properties` (override with environment variables such as `TRACKER_REFRESH_CRON`):

| Property | Purpose |
| --- | --- |
| `tracker.data-url` | CSV time series to load |
| `tracker.refresh-cron` | How often to re-download the data (default: hourly) |

> Note: Johns Hopkins stopped updating this dataset on March 10, 2023, so the figures shown are frozen at that date.
