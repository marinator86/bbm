# B* Branch Manager

An app to monitor branches that are in pull requests / by using webhooks.
Through a connection with a salesforce org, information about sandboxes can get obtained.
A sandbox then gets assigned to a branch. Information of a branch's sandbox can be obtained and used in CI.

WIP!

## Running Locally

Make sure you have Java installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ git clone https://github.com/marinator86/bbm
$ cd bbm
$ ./gradlew stage
$ heroku local web
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

If you're going to use a database, ensure you have a local `.env` file that reads something like this:

```
DATABASE_URL=postgres://localhost:5432/gradle_database_name
```

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)