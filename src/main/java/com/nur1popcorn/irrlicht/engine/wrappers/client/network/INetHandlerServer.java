package com.nur1popcorn.irrlicht.engine.wrappers.client.network;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;

/**
 * The {@link INetHandlerClient} is used to handle packets sent by the server.
 *
 * @see Wrapper
 * @see NetHandlerServer
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
@DiscoveryMethod(checks = Mapper.CUSTOM)
public interface INetHandlerServer extends Wrapper
{

}
