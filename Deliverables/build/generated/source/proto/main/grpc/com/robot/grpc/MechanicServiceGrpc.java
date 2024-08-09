package com.robot.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: robotService.proto")
public final class MechanicServiceGrpc {

  private MechanicServiceGrpc() {}

  public static final String SERVICE_NAME = "com.robot.grpc.MechanicService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.robot.grpc.RobotService.MechanicRequest,
      com.robot.grpc.RobotService.MechanicResponse> getRequestMechanicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RequestMechanic",
      requestType = com.robot.grpc.RobotService.MechanicRequest.class,
      responseType = com.robot.grpc.RobotService.MechanicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.robot.grpc.RobotService.MechanicRequest,
      com.robot.grpc.RobotService.MechanicResponse> getRequestMechanicMethod() {
    io.grpc.MethodDescriptor<com.robot.grpc.RobotService.MechanicRequest, com.robot.grpc.RobotService.MechanicResponse> getRequestMechanicMethod;
    if ((getRequestMechanicMethod = MechanicServiceGrpc.getRequestMechanicMethod) == null) {
      synchronized (MechanicServiceGrpc.class) {
        if ((getRequestMechanicMethod = MechanicServiceGrpc.getRequestMechanicMethod) == null) {
          MechanicServiceGrpc.getRequestMechanicMethod = getRequestMechanicMethod =
              io.grpc.MethodDescriptor.<com.robot.grpc.RobotService.MechanicRequest, com.robot.grpc.RobotService.MechanicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RequestMechanic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.MechanicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.MechanicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MechanicServiceMethodDescriptorSupplier("RequestMechanic"))
              .build();
        }
      }
    }
    return getRequestMechanicMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.robot.grpc.RobotService.AcknowledgeMechanicRequest,
      com.robot.grpc.RobotService.AcknowledgeMechanicResponse> getAcknowledgeMechanicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AcknowledgeMechanic",
      requestType = com.robot.grpc.RobotService.AcknowledgeMechanicRequest.class,
      responseType = com.robot.grpc.RobotService.AcknowledgeMechanicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.robot.grpc.RobotService.AcknowledgeMechanicRequest,
      com.robot.grpc.RobotService.AcknowledgeMechanicResponse> getAcknowledgeMechanicMethod() {
    io.grpc.MethodDescriptor<com.robot.grpc.RobotService.AcknowledgeMechanicRequest, com.robot.grpc.RobotService.AcknowledgeMechanicResponse> getAcknowledgeMechanicMethod;
    if ((getAcknowledgeMechanicMethod = MechanicServiceGrpc.getAcknowledgeMechanicMethod) == null) {
      synchronized (MechanicServiceGrpc.class) {
        if ((getAcknowledgeMechanicMethod = MechanicServiceGrpc.getAcknowledgeMechanicMethod) == null) {
          MechanicServiceGrpc.getAcknowledgeMechanicMethod = getAcknowledgeMechanicMethod =
              io.grpc.MethodDescriptor.<com.robot.grpc.RobotService.AcknowledgeMechanicRequest, com.robot.grpc.RobotService.AcknowledgeMechanicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AcknowledgeMechanic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.AcknowledgeMechanicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.AcknowledgeMechanicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MechanicServiceMethodDescriptorSupplier("AcknowledgeMechanic"))
              .build();
        }
      }
    }
    return getAcknowledgeMechanicMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MechanicServiceStub newStub(io.grpc.Channel channel) {
    return new MechanicServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MechanicServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MechanicServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MechanicServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MechanicServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class MechanicServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void requestMechanic(com.robot.grpc.RobotService.MechanicRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.MechanicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestMechanicMethod(), responseObserver);
    }

    /**
     */
    public void acknowledgeMechanic(com.robot.grpc.RobotService.AcknowledgeMechanicRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.AcknowledgeMechanicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAcknowledgeMechanicMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestMechanicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.robot.grpc.RobotService.MechanicRequest,
                com.robot.grpc.RobotService.MechanicResponse>(
                  this, METHODID_REQUEST_MECHANIC)))
          .addMethod(
            getAcknowledgeMechanicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.robot.grpc.RobotService.AcknowledgeMechanicRequest,
                com.robot.grpc.RobotService.AcknowledgeMechanicResponse>(
                  this, METHODID_ACKNOWLEDGE_MECHANIC)))
          .build();
    }
  }

  /**
   */
  public static final class MechanicServiceStub extends io.grpc.stub.AbstractStub<MechanicServiceStub> {
    private MechanicServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MechanicServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MechanicServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MechanicServiceStub(channel, callOptions);
    }

    /**
     */
    public void requestMechanic(com.robot.grpc.RobotService.MechanicRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.MechanicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestMechanicMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void acknowledgeMechanic(com.robot.grpc.RobotService.AcknowledgeMechanicRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.AcknowledgeMechanicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAcknowledgeMechanicMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MechanicServiceBlockingStub extends io.grpc.stub.AbstractStub<MechanicServiceBlockingStub> {
    private MechanicServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MechanicServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MechanicServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MechanicServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.robot.grpc.RobotService.MechanicResponse requestMechanic(com.robot.grpc.RobotService.MechanicRequest request) {
      return blockingUnaryCall(
          getChannel(), getRequestMechanicMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.robot.grpc.RobotService.AcknowledgeMechanicResponse acknowledgeMechanic(com.robot.grpc.RobotService.AcknowledgeMechanicRequest request) {
      return blockingUnaryCall(
          getChannel(), getAcknowledgeMechanicMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MechanicServiceFutureStub extends io.grpc.stub.AbstractStub<MechanicServiceFutureStub> {
    private MechanicServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MechanicServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MechanicServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MechanicServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.robot.grpc.RobotService.MechanicResponse> requestMechanic(
        com.robot.grpc.RobotService.MechanicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestMechanicMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.robot.grpc.RobotService.AcknowledgeMechanicResponse> acknowledgeMechanic(
        com.robot.grpc.RobotService.AcknowledgeMechanicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAcknowledgeMechanicMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST_MECHANIC = 0;
  private static final int METHODID_ACKNOWLEDGE_MECHANIC = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MechanicServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MechanicServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_MECHANIC:
          serviceImpl.requestMechanic((com.robot.grpc.RobotService.MechanicRequest) request,
              (io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.MechanicResponse>) responseObserver);
          break;
        case METHODID_ACKNOWLEDGE_MECHANIC:
          serviceImpl.acknowledgeMechanic((com.robot.grpc.RobotService.AcknowledgeMechanicRequest) request,
              (io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.AcknowledgeMechanicResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MechanicServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MechanicServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.robot.grpc.RobotService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MechanicService");
    }
  }

  private static final class MechanicServiceFileDescriptorSupplier
      extends MechanicServiceBaseDescriptorSupplier {
    MechanicServiceFileDescriptorSupplier() {}
  }

  private static final class MechanicServiceMethodDescriptorSupplier
      extends MechanicServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MechanicServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MechanicServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MechanicServiceFileDescriptorSupplier())
              .addMethod(getRequestMechanicMethod())
              .addMethod(getAcknowledgeMechanicMethod())
              .build();
        }
      }
    }
    return result;
  }
}
