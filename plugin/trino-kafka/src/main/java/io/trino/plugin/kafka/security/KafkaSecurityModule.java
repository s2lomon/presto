/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.kafka.security;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.airlift.configuration.AbstractConfigurationAwareModule;
import io.trino.plugin.kafka.KafkaSecurityConfig;
import org.apache.kafka.common.security.auth.SecurityProtocol;

import static io.airlift.configuration.ConditionalModule.installModuleIf;
import static io.airlift.configuration.ConfigurationModule.installModules;

public class KafkaSecurityModule
        extends AbstractConfigurationAwareModule
{
    @Override
    protected void setup(Binder binder)
    {
        bindSecurityModule(
                SecurityProtocol.SSL,
                installModules(new SslSecurityModule()));
    }

    private void bindSecurityModule(SecurityProtocol securityProtocol, Module module)
    {
        install(installModuleIf(
                KafkaSecurityConfig.class,
                config -> config.getSecurityProtocol().equals(securityProtocol),
                module));
    }
}