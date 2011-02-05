package com.rossabaker.ivy2mvn

import org.scalatra.ScalatraServlet

class Ivy2MvnServlet extends ScalatraServlet {
  get("/*/:artifactId/:version/:artifactId-:version.pom") {
    <dl>
      <dt>GroupId</dt>
      <dd>{params("splat").replaceAll("/", ".")}</dd>

      <dt>ArtifactId</dt>
      <dd>{params("artifactId")}</dd>

      <dt>Version</dt>
      <dd>{params("version")}</dd>
    </dl>
  }
}