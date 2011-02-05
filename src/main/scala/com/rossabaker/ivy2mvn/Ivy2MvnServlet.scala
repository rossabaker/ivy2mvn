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

class Ivy2MvnServlet extends ScalatraServlet {
  val ivyRoot = "http://databinder.net/repo"

  get("/*/:artifactId/:version/:artifactId-:version.pom") {
    val groupId = params("splat").replaceAll("/", ".")
    val artifactId = params("artifactId")
    val version = params("version")
    val ivyUrl = new URL(List(ivyRoot, groupId, artifactId, version, "ivys", "ivy.xml").mkString("/"))
    val md = XmlModuleDescriptorParser.getInstance.parseDescriptor(new IvySettings, ivyUrl, false)
    // What?!  I can't write to a stream?
    val pomFile = File.createTempFile("ivy2mvn", "pom")
    try {
      PomModuleDescriptorWriter.write(md, pomFile, new PomWriterOptions)
      val in = new FileReader(pomFile)
      try {
        IOUtils.copy(in, response.getWriter)
      }
      finally {
        in.close()
      }
    }
    finally {
      pomFile.delete()
    }
    ()
  }
}
