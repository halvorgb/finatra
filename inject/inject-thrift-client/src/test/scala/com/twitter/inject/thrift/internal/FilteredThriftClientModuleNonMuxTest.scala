package com.twitter.inject.thrift.internal

import com.twitter.greeter.thriftscala.Greeter
import com.twitter.greeter.thriftscala.Greeter.ServiceIface
import com.twitter.inject.app.TestInjector
import com.twitter.inject.modules.StatsReceiverModule
import com.twitter.inject.thrift.filters.ThriftClientFilterBuilder
import com.twitter.inject.thrift.modules.{ThriftClientIdModule, FilteredThriftClientModule}
import com.twitter.inject.{TypeUtils, InjectorModule, IntegrationTest}
import com.twitter.util.Future

class FilteredThriftClientModuleNonMuxTest extends IntegrationTest {

  override val injector = TestInjector(
    modules =
      Seq(FilteredThriftClientModuleNonMux,
        ThriftClientIdModule, StatsReceiverModule,
        InjectorModule),
    flags = Map("com.twitter.server.resolverMap" -> "greeter-thrift-service=nil!"))
    .create

  lazy val greeter =
    injector.instance[Greeter[Future]](TypeUtils.asManifest[Greeter[Future]])

  test("bind expected type properly") {
    assert(greeter != null)
  }

  object FilteredThriftClientModuleNonMux
    extends FilteredThriftClientModule[Greeter[Future], Greeter.ServiceIface] {
    override val label = "greeter-thrift-client"
    override val dest = "flag!greeter-thrift-service"
    override val mux = false

    override def filterServiceIface(
      serviceIface: ServiceIface,
      filter: ThriftClientFilterBuilder) = {

      serviceIface
    }
  }

}
