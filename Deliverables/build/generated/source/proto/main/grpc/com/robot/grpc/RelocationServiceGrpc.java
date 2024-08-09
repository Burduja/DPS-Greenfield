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
public final class RelocationServiceGrpc {

  private RelocationServiceGrpc() {}

  public static final String SERVICE_NAME = "com.robot.grpc.RelocationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.robot.grpc.RobotService.RelocationRequest,
      com.robot.grpc.RobotService.RelocationResponse> getRequestRelocationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RequestRelocation",
      requestType = com.robot.grpc.RobotService.RelocationRequest.class,
      responseType = com.robot.grpc.RobotService.RelocationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.robot.grpc.RobotService.RelocationRequest,
      com.robot.grpc.RobotService.RelocationResponse> getRequestRelocationMethod() {
    io.grpc.MethodDescriptor<com.robot.grpc.RobotService.RelocationRequest, com.robot.grpc.RobotService.RelocationResponse> getRequestRelocationMethod;
    if ((getRequestRelocationMethod = RelocationServiceGrpc.getRequestRelocationMethod) == null) {
      synchronized (RelocationServiceGrpc.class) {
        if ((getRequestRelocationMethod = RelocationServiceGrpc.getRequestRelocationMethod) == null) {
          RelocationServiceGrpc.getRequestRelocationMethod = getRequestRelocationMethod =
              io.grpc.MethodDescriptor.<com.robot.grpc.RobotService.RelocationRequest, com.robot.grpc.RobotService.RelocationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RequestRelocation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.RelocationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.RelocationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RelocationServiceMethodDescriptorSupplier("RequestRelocation"))
              .build();
        }
      }
    }
    return getRequestRelocationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.robot.grpc.RobotService.AcknowledgeRelocationRequest,
      com.robot.grpc.RobotService.AcknowledgeRelocationResponse> getAcknowledgeRelocationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AcknowledgeRelocation",
      requestType = com.robot.grpc.RobotService.AcknowledgeRelocationRequest.class,
      responseType = com.robot.grpc.RobotService.AcknowledgeRelocationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.robot.grpc.RobotService.AcknowledgeRelocationRequest,
      com.robot.grpc.RobotService.AcknowledgeRelocationResponse> getAcknowledgeRelocationMethod() {
    io.grpc.MethodDescriptor<com.robot.grpc.RobotService.AcknowledgeRelocationRequest, com.robot.grpc.RobotService.AcknowledgeRelocationResponse> getAcknowledgeRelocationMethod;
    if ((getAcknowledgeRelocationMethod = RelocationServiceGrpc.getAcknowledgeRelocationMethod) == null) {
      synchronized (RelocationServiceGrpc.class) {
        if ((getAcknowledgeRelocationMethod = RelocationServiceGrpc.getAcknowledgeRelocationMethod) == null) {
          RelocationServiceGrpc.getAcknowledgeRelocationMethod = getAcknowledgeRelocationMethod =
              io.grpc.MethodDescriptor.<com.robot.grpc.RobotService.AcknowledgeRelocationRequest, com.robot.grpc.RobotService.AcknowledgeRelocationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AcknowledgeRelocation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.AcknowledgeRelocationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.AcknowledgeRelocationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RelocationServiceMethodDescriptorSupplier("AcknowledgeRelocation"))
              .build();
        }
      }
    }
    return getAcknowledgeRelocationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.robot.grpc.RobotService.MovingRequest,
      com.robot.grpc.RobotService.MovingResponse> getMovingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Moving",
      requestType = com.robot.grpc.RobotService.MovingRequest.class,
      responseType = com.robot.grpc.RobotService.MovingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.robot.grpc.RobotService.MovingRequest,
      com.robot.grpc.RobotService.MovingResponse> getMovingMethod() {
    io.grpc.MethodDescriptor<com.robot.grpc.RobotService.MovingRequest, com.robot.grpc.RobotService.MovingResponse> getMovingMethod;
    if ((getMovingMethod = RelocationServiceGrpc.getMovingMethod) == null) {
      synchronized (RelocationServiceGrpc.class) {
        if ((getMovingMethod = RelocationServiceGrpc.getMovingMethod) == null) {
          RelocationServiceGrpc.getMovingMethod = getMovingMethod =
              io.grpc.MethodDescriptor.<com.robot.grpc.RobotService.MovingRequest, com.robot.grpc.RobotService.MovingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Moving"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.MovingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.MovingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RelocationServiceMethodDescriptorSupplier("Moving"))
              .build();
        }
      }
    }
    return getMovingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RelocationServiceStub newStub(io.grpc.Channel channel) {
    return new RelocationServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RelocationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RelocationServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RelocationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RelocationServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class RelocationServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void requestRelocation(com.robot.grpc.RobotService.RelocationRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.RelocationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestRelocationMethod(), responseObserver);
    }

    /**
     */
    public void acknowledgeRelocation(com.robot.grpc.RobotService.AcknowledgeRelocationRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.AcknowledgeRelocationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAcknowledgeRelocationMethod(), responseObserver);
    }

    /**
     */
    public void moving(com.robot.grpc.RobotService.MovingRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.MovingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMovingMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestRelocationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.robot.grpc.RobotService.RelocationRequest,
                com.robot.grpc.RobotService.RelocationResponse>(
                  this, METHODID_REQUEST_RELOCATION)))
          .addMethod(
            getAcknowledgeRelocationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.robot.grpc.RobotService.AcknowledgeRelocationRequest,
                com.robot.grpc.RobotService.AcknowledgeRelocationResponse>(
                  this, METHODID_ACKNOWLEDGE_RELOCATION)))
          .addMethod(
            getMovingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.robot.grpc.RobotService.MovingRequest,
                com.robot.grpc.RobotService.MovingResponse>(
                  this, METHODID_MOVING)))
          .build();
    }
  }

  /**
   */
  public static final class RelocationServiceStub extends io.grpc.stub.AbstractStub<RelocationServiceStub> {
    private RelocationServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RelocationServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RelocationServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RelocationServiceStub(channel, callOptions);
    }

    /**
     */
    public void requestRelocation(com.robot.grpc.RobotService.RelocationRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.RelocationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestRelocationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void acknowledgeRelocation(com.robot.grpc.RobotService.AcknowledgeRelocationRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.AcknowledgeRelocationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAcknowledgeRelocationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void moving(com.robot.grpc.RobotService.MovingRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.MovingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMovingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RelocationServiceBlockingStub extends io.grpc.stub.AbstractStub<RelocationServiceBlockingStub> {
    private RelocationServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RelocationServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RelocationServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RelocationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.robot.grpc.RobotService.RelocationResponse requestRelocation(com.robot.grpc.RobotService.RelocationRequest request) {
      return blockingUnaryCall(
          getChannel(), getRequestRelocationMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.robot.grpc.RobotService.AcknowledgeRelocationResponse acknowledgeRelocation(com.robot.grpc.RobotService.AcknowledgeRelocationRequest request) {
      return blockingUnaryCall(
          getChannel(), getAcknowledgeRelocationMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.robot.grpc.RobotService.MovingResponse moving(com.robot.grpc.RobotService.MovingRequest request) {
      return blockingUnaryCall(
          getChannel(), getMovingMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RelocationServiceFutureStub extends io.grpc.stub.AbstractStub<RelocationServiceFutureStub> {
    private RelocationServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RelocationServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RelocationServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RelocationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.robot.grpc.RobotService.RelocationResponse> requestRelocation(
        com.robot.grpc.RobotService.RelocationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestRelocationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.robot.grpc.RobotService.AcknowledgeRelocationResponse> acknowledgeRelocation(
        com.robot.grpc.RobotService.AcknowledgeRelocationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAcknowledgeRelocationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.robot.grpc.RobotService.MovingResponse> moving(
        com.robot.grpc.RobotService.MovingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMovingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST_RELOCATION = 0;
  private static final int METHODID_ACKNOWLEDGE_RELOCATION = 1;
  private static final int METHODID_MOVING = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RelocationServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RelocationServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_RELOCATION:
          serviceImpl.requestRelocation((com.robot.grpc.RobotService.RelocationRequest) request,
              (io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.RelocationResponse>) responseObserver);
          break;
        case METHODID_ACKNOWLEDGE_RELOCATION:
          serviceImpl.acknowledgeRelocation((com.robot.grpc.RobotService.AcknowledgeRelocationRequest) request,
              (io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.AcknowledgeRelocationResponse>) responseObserver);
          break;
        case METHODID_MOVING:
          serviceImpl.moving((com.robot.grpc.RobotService.MovingRequest) request,
              (io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.MovingResponse>) responseObserver);
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

  private static abstract class RelocationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RelocationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.robot.grpc.RobotService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RelocationService");
    }
  }

  private static final class RelocationServiceFileDescriptorSupplier
      extends RelocationServiceBaseDescriptorSupplier {
    RelocationServiceFileDescriptorSupplier() {}
  }

  private static final class RelocationServiceMethodDescriptorSupplier
      extends RelocationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RelocationServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (RelocationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RelocationServiceFileDescriptorSupplier())
              .addMethod(getRequestRelocationMethod())
              .addMethod(getAcknowledgeRelocationMethod())
              .addMethod(getMovingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
