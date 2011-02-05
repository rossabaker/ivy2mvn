package com.rossabaker.ivy2mvn

import java.net.URL
import sbt._

class Ivy2MvnProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val scalatra = "org.scalatra" %% "scalatra" % "2.0.0-SNAPSHOT" % "compile"
  val ivy = "org.apache.ivy" % "ivy" % "2.2.0" % "compile"
  val commonsIo = "commons-io" % "commons-io" % "2.0.1" % "compile"
  val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % "7.2.2.v20101205" % "test"

  val sonatypeNexusSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val dbRepo = Resolver.url("technically.us", new URL("http://databinder.net/repo/"))(Resolver.ivyStylePatterns)
}
