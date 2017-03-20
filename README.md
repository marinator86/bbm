# dynOrg: CI for large force.com projects

Do you want to benefit from continuous integration also in force.com projects? Are you fed up with the clumsy reviewing of pull requests especially in large projects with many PRs? We felt wasting lots of time, repeating the same time-consuming steps again and again:

- Check out the review branch
- Get some free sandbox to deploy the branch to
- Deploy the review branch to the sandbox (using the [force.com migration tool](https://developer.salesforce.com/page/Force.com_Migration_Tool))
- Since build errors may occur, now you either fix them or you note this down for someone else
- When the build succeeds, you can finally start reviewing..

What if you could be freed from that burden? Reviewing could be as easy as reading the access information of the review org from CI, then, you'd simply login to the review org and perform your review. Sounds easy, right?

This is how it could look like:
![alt tag](https://picload.org/image/rlipwrpw/dynorg.png)

## Running Locally

Make sure you have Java installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

This is what we're currently working on. We are logicline.de, a leading German salesforce PDO. If you'd like to join forces with us, feel free to contact us: mario.hammer@logicline.de

If you'd like to have a look at dynorg, please install a mongodb somewhere (how about mlab?), and ensure you have a local `.env` file in the repo that reads like this:
```
MONGODB_URI=mongodb://user:password@ds165329.mlab.com:55529/heroku_5gj7iu5b
```

then, execute the following command:
```sh
$ git clone https://github.com/marinator86/bbm
$ cd bbm
$ ./gradlew stage
$ heroku local web
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

The dynOrg server offers a REST API for control. The API is still WIP, but in its final state it will look like [this](https://app.swaggerhub.com/api/marinator86/dyn-org_api/1.0.0).

More documentation is about to follow soon. In case you'd like to know more, please ask me at mario.hammer@logicline.de :-)


## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```
