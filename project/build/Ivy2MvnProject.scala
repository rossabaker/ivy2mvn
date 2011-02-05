package com.rossabaker.ivy2mvn

import sbt._

class Ivy2MvnProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val scalatra = "org.scalatra" %% "scalatra" % "2.0.0.M2" % "compile"
  val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % "7.2.2.v20101205" % "test"
}
