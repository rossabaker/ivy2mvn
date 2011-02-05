package com.rossabaker.ivy2mvn

import org.scalatra.ScalatraServlet
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorParser
import java.net.URL
import io.Source
import java.io.{FileReader, File}
import org.apache.commons.io.IOUtils
import org.apache.ivy.plugins.parser.m2.PomWriterOptions.ConfigurationScopeMapping
import org.apache.ivy.plugins.parser.m2.{PomWriterOptions, PomModuleDescriptorWriter}
import java.text.ParseException
import scala.collection.JavaConversions._

class Ivy2MvnServlet extends ScalatraServlet {
  var ivyRoot: String = _

  get("/*/:artifactId/:version/:artifactId-:version.:ext") {
    val groupId = params("splat").replaceAll("/", ".")
    val artifactId = params("artifactId")
    val version = params("version")
    val ext = params("ext")
    val ivyUrl = List(ivyRoot, groupId, artifactId, version, ext+"s", artifactId+"."+ext).mkString("/")
    redirect(ivyUrl)
  }

  get("/*/:artifactId/:version/:artifactId-:version.pom") {
    val groupId = params("splat").replaceAll("/", ".")
    val artifactId = params("artifactId")
    val version = params("version")
    val md = parseModuleDescriptor(groupId, artifactId, version)
    writePom(md)
  }


  protected val mapping: ConfigurationScopeMapping = {
    val confs: Seq[String] =  "compile" :: "runtime" :: "test" :: "provided" :: "system" :: Nil
    val mapping = Map(confs map { conf => (conf, conf) } : _*) + ("default" -> "compile")
    new ConfigurationScopeMapping(mapping)
  }

  private def parseModuleDescriptor(groupId: String, artifactId: String, version: String) = {
    val ivyUrl = new URL(List(ivyRoot, groupId, artifactId, version, "ivys", "ivy.xml").mkString("/"))
    try {
      XmlModuleDescriptorParser.getInstance.parseDescriptor(new IvySettings, ivyUrl, false)
    }
    catch {
      case e: ParseException => halt(404)
    }
  }

  private def writePom(md: ModuleDescriptor): Unit =
    // What?!  I can't write to a stream?
    withTempFile("ivy2mvn", Some("pom")) { pomFile =>
      val options = (new PomWriterOptions).setMapping(mapping)
      PomModuleDescriptorWriter.write(md, pomFile, options)
      val in = new FileReader(pomFile)
      try {
        IOUtils.copy(in, response.getWriter)
      }
      finally {
        in.close()
      }
    }

  private def withTempFile[A](prefix: String = "ivy2mvn", suffix: Option[String] = None)(f: File => A) = {
    val file = File.createTempFile(prefix, suffix.getOrElse(null))
    try {
      f(file)
    }
    finally {
      file.delete()
    }
  }

  override def init = {
    ivyRoot = getServletConfig.getInitParameter("ivy.root")
  }
}
