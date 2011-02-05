# ivy2mvn

This project proxies an Ivy repository in a Maven2 layout.  The intent is to let Maven builds depend on artifacts deployed only with an Ivy descriptor.

## Running

In web.xml, tweak the init-param `ivy.root` to the URL that you want to proxy.  Run `sbt jetty`, or `sbt package` and deploy to your servlet container of choice.

## Features

- Generates POMs on the fly from the proxied ivy.xml.
- Redirects non-POM requests to the proxied artifact by naming convention.

## TODO

- Does not read the ivy.xml to find artifacts in non-standard locations.
- Cache generated POMs.
- Smarter mapping of Ivy configurations to Maven scopes?
